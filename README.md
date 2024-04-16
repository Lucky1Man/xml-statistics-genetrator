# Steps to run this project 
### 1) Clone the repository
### 2) run ```maven package``` inside cloned dir
### 3) run ```maven exec:exec``` inside cloned dir
The output will be inside the directory from which you were running maven commands, if you are running commands outside of cloned dir you will need to adjust --JSON_DIR argument.<br>
The project contains some dummy data inside resources dir.<br>
You can also use the generate-json.py script located in resources/scripts.

### Entities of this project: 
![image](https://github.com/Lucky1Man/xml-statistics-genetrator/assets/86126779/788b652d-5679-4c8a-8fb8-2a3cc6142b9e)
#### executorId - is a foreign key for Participant.id

# You can check the project structure below.
#### I designed it considering the fact that I will integrate it into a bigger project later. If I did not know that for 100%, I would have stuck to a much simpler solution.
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
