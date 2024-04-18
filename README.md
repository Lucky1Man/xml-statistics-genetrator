# Steps to run this project 
### 1) Clone the repository
### 2) run ```maven package``` inside cloned dir
### 3) run ```maven exec:exec``` inside cloned dir
The output will be inside the directory from which you were running maven commands, if you are running commands outside of cloned dir you will need to adjust --JSON_DIR argument.<br>
The project contains some dummy data inside resources dir.<br>
You can also use the generate-json.py script located in resources/scripts.<br>
### You can find examples of input files inside resources/medium, and output files inside the root of this repository: statistics_by_executorId.xml, statistics_by_guarantorEmails.xml 

### Entities of this project: 
![image](https://github.com/Lucky1Man/xml-statistics-genetrator/assets/86126779/788b652d-5679-4c8a-8fb8-2a3cc6142b9e)
#### executorId - is a foreign key for Participant.id

# Concurrency tests
My machine has 6 real cores.<br>
Short conclusion: The difference can be seen with more files and bigger sizes of files. <br>
All tests were made on unlimited RAM. If I limit max ram usage to 40mb(when I process 4 files each 100_000 records their total size is more than 300mb) the difference will be seen only on vast amounts of data and will be around ~50-100 milliseconds.
## 2 files with 10_000 records per each file
### 1 thread
![image](https://github.com/Lucky1Man/xml-statistics-genetrator/assets/86126779/0e836711-2b3a-4af9-8041-a2ffb3697379)
![image](https://github.com/Lucky1Man/xml-statistics-genetrator/assets/86126779/3089ce03-2a7e-4aac-aee0-3c57974ccbe0)
![image](https://github.com/Lucky1Man/xml-statistics-genetrator/assets/86126779/6d57d39d-0b0f-418b-8897-5dcbf93fce03)
### 2 threads 
![image](https://github.com/Lucky1Man/xml-statistics-genetrator/assets/86126779/442f9755-6dd8-4053-8982-57f4291838ad)
![image](https://github.com/Lucky1Man/xml-statistics-genetrator/assets/86126779/7bce2b41-41b7-45be-bd83-c357e83f0ac7)
![image](https://github.com/Lucky1Man/xml-statistics-genetrator/assets/86126779/bde63b69-b3e1-404d-ad7f-5c10e5e8c7c4)
### 4 threads 
![image](https://github.com/Lucky1Man/xml-statistics-genetrator/assets/86126779/010d721f-9b21-4f90-bdd6-4e2d316733d5)
![image](https://github.com/Lucky1Man/xml-statistics-genetrator/assets/86126779/e0fe4490-fd4e-4205-b363-ec0f22851bc4)
![image](https://github.com/Lucky1Man/xml-statistics-genetrator/assets/86126779/f5bf55ca-8f76-4af1-8c68-09ff8d7a7542)
### 8 threads 
![image](https://github.com/Lucky1Man/xml-statistics-genetrator/assets/86126779/5c4c43e1-fa23-41ea-b0d2-d94aa90c6765)
![image](https://github.com/Lucky1Man/xml-statistics-genetrator/assets/86126779/95a134c8-edac-4d63-928a-f901b76530c9)
![image](https://github.com/Lucky1Man/xml-statistics-genetrator/assets/86126779/7ef2cbe4-0590-4277-a928-2f5b00216131)
## 2 files 100_000 records per each file
### 1 thread 
![image](https://github.com/Lucky1Man/xml-statistics-genetrator/assets/86126779/fc3068a2-8b6e-4346-8db7-f9647e4d2715)
![image](https://github.com/Lucky1Man/xml-statistics-genetrator/assets/86126779/7d14ba57-211d-42c3-a104-e5a146c1cdf4)
![image](https://github.com/Lucky1Man/xml-statistics-genetrator/assets/86126779/1eb31164-e5be-4d8d-9f04-ae24cc3a9479)
### 2 threads 
![image](https://github.com/Lucky1Man/xml-statistics-genetrator/assets/86126779/42df60c5-a099-4c04-a6db-0cb21c512c42)
![image](https://github.com/Lucky1Man/xml-statistics-genetrator/assets/86126779/a551239d-fcd8-477a-a148-5163ea34f63f)
![image](https://github.com/Lucky1Man/xml-statistics-genetrator/assets/86126779/1405f13a-c770-49d2-bbe4-2a6cea4df67b)
### 4 threads
![image](https://github.com/Lucky1Man/xml-statistics-genetrator/assets/86126779/6356a4d0-1b01-4aa5-a86e-a40a6163b988)
![image](https://github.com/Lucky1Man/xml-statistics-genetrator/assets/86126779/e40af810-89a1-4268-8e5d-aa906236d7b2)
![image](https://github.com/Lucky1Man/xml-statistics-genetrator/assets/86126779/bb39ddeb-9bde-48ec-852f-aa7a2749865c)
### 8 threads 
![image](https://github.com/Lucky1Man/xml-statistics-genetrator/assets/86126779/68810cfe-c136-414c-b36c-1278e819c8f7)
![image](https://github.com/Lucky1Man/xml-statistics-genetrator/assets/86126779/22adf16c-200e-45ab-bf51-9ff7c797cd03)
![image](https://github.com/Lucky1Man/xml-statistics-genetrator/assets/86126779/121135f1-cf2c-4b81-80fd-cf93c6cd80e2)
## 4 files 100_000 records per each file
### 1 thread
![image](https://github.com/Lucky1Man/xml-statistics-genetrator/assets/86126779/1fa25f2e-b2be-4954-a5d9-5dea42dc7b0d)
![image](https://github.com/Lucky1Man/xml-statistics-genetrator/assets/86126779/70e3fb5b-6b91-4c23-8387-f03a59a6551d)
![image](https://github.com/Lucky1Man/xml-statistics-genetrator/assets/86126779/2984641e-8a4a-4837-97f8-85abb15dfe21)
### 2 threads
![image](https://github.com/Lucky1Man/xml-statistics-genetrator/assets/86126779/2cf834c2-be79-4e24-937c-e1fce04af450)
![image](https://github.com/Lucky1Man/xml-statistics-genetrator/assets/86126779/ca4255ea-e637-4588-83d2-42294b408557)
![image](https://github.com/Lucky1Man/xml-statistics-genetrator/assets/86126779/b6741305-022b-4248-b219-71c29e27ef37)
### 4 threads
![image](https://github.com/Lucky1Man/xml-statistics-genetrator/assets/86126779/0b4419ff-1dd6-44a8-8711-371945d613e9)
![image](https://github.com/Lucky1Man/xml-statistics-genetrator/assets/86126779/7296a41c-e569-4979-8069-d2b6a146fd29)
![image](https://github.com/Lucky1Man/xml-statistics-genetrator/assets/86126779/49ca7d5a-6bff-4469-92da-d0c495499df2)
### 8 threads
![image](https://github.com/Lucky1Man/xml-statistics-genetrator/assets/86126779/71230c37-1520-47ff-af31-6cc39aea9349)
![image](https://github.com/Lucky1Man/xml-statistics-genetrator/assets/86126779/d8251d1b-c9f3-48c4-bdb8-1806b5f14cc7)
![image](https://github.com/Lucky1Man/xml-statistics-genetrator/assets/86126779/55d314bb-72ec-48d1-a0cc-a1b57ee5d66e)

# You can check the project structure below.
#### I designed it considering two main facts:
##### 1) I want to show the ability to understand and create complex structures.
##### 2) I will integrate it into a bigger project later.
#### If not for these 2 facts, I would have stuck to a much simpler solution.
#### You can find detailed documentation inside classes.

## The main benefits of this solution
#### Multiple fields can be processed during one execution.
#### This solution allows me to add support for new fields easily.
#### It allows to have multiple output providers.
#### You can set up any JSON source.

## Main processing class is JsonStatisticsProcessor. 
#### When calling JsonStatisticsProcessor::run it creates a new instance of JsonFieldValuesReader for each JsonParser provided and all the instances are run using ThreadPool
#### When processing of all JsonParser is finished all the statistic is passed to consumers.
#### You can set up any consumers, I used XmlStatisticsWriter which you can check in this repository.
#### You can also gather statistics from any JSON source supported by JsonParser
![image](https://github.com/Lucky1Man/xml-statistics-genetrator/assets/86126779/0f0418df-df49-4b56-93c0-a7f45eb1f480)
## Main reading class is JsonFieldValuesReader.
![image](https://github.com/Lucky1Man/xml-statistics-genetrator/assets/86126779/75c257a6-84a7-4713-8493-7d558a68dd7f)
## Structure of field listeners 
![image](https://github.com/Lucky1Man/xml-statistics-genetrator/assets/86126779/85931422-c999-46b2-ac99-b4fb4d406395)

### To add support for a new field you can simply implement StatisticsByFieldService and pass it to JsonStatisticsProcessor
