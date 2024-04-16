import json
import random
import uuid

from dateutil.relativedelta import relativedelta
from faker import Faker

NUMBER_OF_RECORDS = 10_000
FILE_PATH = "../resources/medium/data6.json"
# FILE_PATH = "../../test/resources/test-facts-source.json"

MINIMAL_NUMBER_OF_PARTICIPANTS = 3
MINIMAL_NUMBER_OF_DESCRIPTIONS = 4
MINIMAL_NUMBER_OF_START_FINISH = 6
MINIMAL_NUMBER_OF_GUARANTORS = 1
MAXIMAL_NUMBER_OF_GUARANTORS = 15
MINIMAL_NUMBER_OF_GUARANTOR_SETS = 3

fake = Faker()


def get_random_element(list):
    return list[random.randint(0, len(list)-1)]


def at_least(value, min_value):
    if value < min_value:
        return min_value
    return value


emails = []
executorIds = []
descriptions = []
startTimeToEndTime = []
guarantorEmails = []

dummyJsonObjects = []

for _ in range(at_least(int(NUMBER_OF_RECORDS / 6.666), MINIMAL_NUMBER_OF_PARTICIPANTS)):
    emails.append(fake.email())
    executorIds.append(str(uuid.uuid4()))

for _ in range(at_least(int(NUMBER_OF_RECORDS / 4), MINIMAL_NUMBER_OF_DESCRIPTIONS)):
    descriptions.append(fake.text(max_nb_chars=500))

for _ in range(at_least(int(NUMBER_OF_RECORDS / 2), MINIMAL_NUMBER_OF_START_FINISH)):
    startTime = fake.date_time()
    endDate = startTime + relativedelta(years=5)
    startTimeToEndTime.append(
            (
                startTime.strftime('%Y-%m-%dT%H:%M:%S.%f'),
                (fake.date_time_between(start_date=(startTime + relativedelta(days=1)), end_date=endDate)).strftime('%Y-%m-%dT%H:%M:%S.%f')
            )
    )

for _ in range(at_least(int(NUMBER_OF_RECORDS / 16.666), MINIMAL_NUMBER_OF_GUARANTOR_SETS)):
    numberOfGuarantors = random.randint(
            MINIMAL_NUMBER_OF_GUARANTORS,
            MAXIMAL_NUMBER_OF_GUARANTORS if len(emails) >= MAXIMAL_NUMBER_OF_GUARANTORS else len(emails)
    )
    guarantors = []
    if numberOfGuarantors == len(emails):
        guarantors.extend(emails)
    else:
        for _ in range(numberOfGuarantors):
            emailsCopy = []
            emailsCopy.extend(emails)
            randomEmail = get_random_element(emailsCopy)
            guarantors.append(randomEmail)
            emailsCopy.remove(randomEmail)
    guarantorEmails.append(', '.join(guarantors))

for _ in range(NUMBER_OF_RECORDS):
    startFinishTimes = get_random_element(startTimeToEndTime)
    dummyJsonObjects.append(
            {
                'executorId': get_random_element(executorIds),
                'description': get_random_element(descriptions),
                'startTime': startFinishTimes[0],
                'finishTime': startFinishTimes[1],
                'guarantorEmails': get_random_element(guarantorEmails)
            }
    )

with open(FILE_PATH, "w") as json_file:
    json.dump(dummyJsonObjects, json_file, indent=4)
