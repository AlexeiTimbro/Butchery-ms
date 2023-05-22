#!/usr/bin/env bash
#
# Sample usage:
#   ./test_all.bash start stop
#   start and stop are optional
#
#   HOST=localhost PORT=7000 ./test-em-all.bash
#
# When not in Docker
#: ${HOST=localhost}
#: ${PORT=7000}

# When in Docker
# shellcheck disable=SC2223
: ${HOST=localhost}
# shellcheck disable=SC2223
: ${PORT=8080}

#array to hold all our test data ids
allTestPurchasesIds=()
allTestCustomersIds=()
allTestMeatsIds=()
allTestButcherIds=()

function assertCurl() {

  local expectedHttpCode=$1
  local curlCmd="$2 -w \"%{http_code}\""
  # shellcheck disable=SC2155
  local result=$(eval $curlCmd)
  local httpCode="${result:(-3)}"
  RESPONSE='' && (( ${#result} > 3 )) && RESPONSE="${result%???}"

  if [ "$httpCode" = "$expectedHttpCode" ]
  then
    if [ "$httpCode" = "200" ]
    then
      echo "Test OK (HTTP Code: $httpCode)"
    else
      echo "Test OK (HTTP Code: $httpCode, $RESPONSE)"
    fi
  else
      echo  "Test FAILED, EXPECTED HTTP Code: $expectedHttpCode, GOT: $httpCode, WILL ABORT!"
      echo  "- Failing command: $curlCmd"
      echo  "- Response Body: $RESPONSE"
      exit 1
  fi
}

function assertEqual() {

  local expected=$1
  local actual=$2

  if [ "$actual" = "$expected" ]
  then
    echo "Test OK (actual value: $actual)"
  else
    echo "Test FAILED, EXPECTED VALUE: $expected, ACTUAL VALUE: $actual, WILL ABORT"
    exit 1
  fi
}

function testUrl() {
    # shellcheck disable=SC2124
    url=$@
    if curl $url -ks -f -o /dev/null
    then
          echo "Ok"
          return 0
    else
          echo -n "not yet"
          return 1
    fi;
}

#prepare the test data that will be passed in the curl commands for posts and puts
function setupTestData() {

body=\
'{
"purchaseId": "a9485060-efc4-11ed-a05b-0242ac120003",
"customerId":"c3540a89-cb47-4c96-888e-ff96708db4d8",
"meatId":"9034bbbb-5d02-443c-9112-9661282befe1",
"butcherId":"075d5fae-9c99-42f0-91c6-f4ec3d256af9",
"salePrice": 45.45,
"purchaseStatus":"PURCHASE_COMPLETED",
"paymentMethod":"CREDIT",
"purchaseDate":"2023-04-20"
}'
recreatePurchaseAggregate 1 "$body" "c3540a89-cb47-4c96-888e-ff96708db4d8"

#CREATE SOME CUSTOMER TEST DATA
 body1=\
'{
    "firstName": "alex",
    "lastName": "alex",
    "email": "alex@prweb.com",
    "phoneNumber": "514-123-4566",
    "street": "12345 alex Road",
    "city": "Rio",
    "province": "Quebec",
    "country": "Senegal",
    "postalCode": "5R6 Joe"
}'
recreateCustomer 1 "$body1"

#CREATE Meat TEST DATA
body2=\
'{
    "animal": "Leaf",
    "status": "AVAILABLE",
    "environment": "Grass",
    "texture": "Grassy",
    "expirationDate": "24-08-2030",
    "price": 20
}'
recreateMeat 1 "$body2"

#CREATE DeliveryDriver TEST DATA

body3=\
'{
    "firstName": "Joe",
    "lastName": "Joe",
    "age": 20,
    "email": "joe@sciencedirect.com",
    "phoneNumber": "514-123-4567",
    "street": "23188 joe Point",
    "city": "Lewisjoe",
    "province": "Newfoundjoe and Labrajoe",
    "country": "Canajoe",
    "postalCode": "5R6 joe",
    "salary": 100000.31,
    "commissionRate": 5
}'
recreateButcher 1 "$body3"

}

#USING PURCHASE TEST DATA - EXECUTE POST REQUEST
function recreatePurchaseAggregate() {
    local testId=$1
    local aggregate=$2
    local customerId=$3

    purchaseId=$(curl -X POST http://$HOST:$PORT/api/v1/customers/$customerId/purchases -H "Content-Type:
    application/json" --data "$aggregate" | jq '.purchaseId')
    echo "Response: $purchaseId"
    allTestPurchasesIds[$testId]=$purchaseId
    echo "Added Purchase Aggregate with PurchaseId: ${allTestPurchasesIds[$testId]}"
}

function recreateCustomer(){
  local testId=$1
  local aggregate=$2

  customerId=$(curl -X POST http://$HOST:$PORT/api/v1/customers -H "Content-Type:
  application/json" --data "$aggregate" | jq '.customerId')
  echo "Response: $customerId"
  allTestCustomersIds[$testId]=$customerId
  echo "Added Customer with customerId: ${allTestCustomersIds[$testId]}"
}

function recreateButcher() {
  local testId=$1
  local aggregate=$2

  butcherId=$(curl -X POST http://$HOST:$PORT/api/v1/butchers -H "Content-Type:
  application/json" --data "$aggregate" | jq '.butcherId')
  echo "Response: $butcherId"
  allTestButcherIds[$testId]=$butcherId
  echo "Added Butcher with butcherId: ${allTestButcherIds[$testId]}"
}


function recreateMeat() {
    local testId=$1
    local aggregate=$2

    meatId=$(curl -X POST http://$HOST:$PORT/api/v1/meats -H "Content-Type:
    application/json" --data "$aggregate" | jq '.meatId')
    echo "Response: $meatId"
    allTestMeatsIds[$testId]=$meatId
    echo "Added Meat with meatId: ${allTestMeatsIds[$testId]}"
}

#don't start testing until all the microservices are up and running
function waitForService() {
    # shellcheck disable=SC2124
    url=$@
    echo -n "Wait for: $url... "
    n=0
    until testUrl $url
    do
        n=$((n + 1))
        if [[ $n == 100 ]]
        then
            echo " Give up"
            exit 1
        else
            sleep 6
            echo -n ", retry #$n "
        fi
    done
}

#start of test script
set -e

echo "HOST=${HOST}"
echo "PORT=${PORT}"

# shellcheck disable=SC2199
if [[ $@ == *"start"* ]]
then
    echo "Restarting the test environment..."
    echo "$ docker-compose down"
    docker-compose down
    echo "$ docker-compose up -d"
    docker-compose up -d
fi

#try to delete an entity/aggregate that you've set up but that you don't need. This will confirm that things are working
#I've set up an inventory with no menus in it

waitForService "curl -X DELETE http://$HOST:$PORT/api/v1/customers/2fde646d-0c9e-43b6-b2b7-526d151a594d"

setupTestdata

#EXECUTE EXPLICIT TESTS AND VALIDATE RESPONSE
## Verify that a normal get by id of earlier posted order works

echo -e "\nTest 1: Verify that a normal get by id of earlier posted purchase works"
assertCurl 200 "curl http://$HOST:$PORT/api/v1/customers/c3540a89-cb47-4c96-888e-ff96708db4d8/purchases/${allTestPurchasesIds[1]} -s"
assertEqual ${allTestPurchasesIds[1]} $(echo $RESPONSE | jq .purchaseId)
assertEqual "\"Patrick\"" $(echo $RESPONSE | jq ".firstName")


#Verify customers get all
echo -e "\Test 2: Verify that a get all customers works"
assertCurl 200 "curl http://$HOST:$PORT/api/v1/customers -s"
assertEqual 6 $(echo $RESPONSE | jq '. | length')

##Verify that a normal post of customers works
echo -e "\nTest 3: Verify that a normal post of customers works"

#assertCurl 201 "curl -X POST http://$HOST:$PORT/api/v1/customers -H \"Content-Type: application/json\" --data '${body2}' -s"

assertCurl 201 "curl -X POST http://$HOST:$PORT/api/v1/customers -H \"Content-Type: application/json\" --data
'{ \"firstName\": \"firstName1\", \"lastName\": \"lastName1\", \"email\": \"alex@prweb.com\", \"phoneNumber\": \"514-123-4566\",
 \"street\": \"12345 alex Road\", \"city\": \"Rio\", \"province\": \"street123\", \"country\": \"Senegal\",
  \"postalCode\": \"5R6 Joe\" }' -s"


assertEqual "\"firstName1\"" $(echo $RESPONSE | jq .firstName)
assertEqual "\"lastName1\"" $(echo $RESPONSE | jq .lastName)
assertEqual "\"alex@prweb.com\"" $(echo $RESPONSE | jq .email)
assertEqual "\"514-123-4566\"" $(echo $RESPONSE | jq .phoneNumber)
assertEqual "\"12345 alex Road\"" $(echo $RESPONSE | jq .street)
assertEqual "\"Rio\"" $(echo $RESPONSE | jq .city)
assertEqual "\"Quebec\"" $(echo $RESPONSE | jq .province)
assertEqual "\"Senegal\"" $(echo $RESPONSE | jq .country)
assertEqual "\"5R6 Joe\"" $(echo $RESPONSE | jq .postalCode)


# shellcheck disable=SC2199
if [[ $@ == *"stop"* ]]
then
    echo "We are done, stopping the test environment..."
    echo "$ docker-compose down"
    docker-compose down
fi