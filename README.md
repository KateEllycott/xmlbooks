# BookXML
Spring-based application which downloads all the fragments of the books and generate tables of contents. Tables of contents exposed via REST API

## Getting started
Go to the target directory of the project and execute with parameters  
inputFile={input file should be in csv format}
importFolder={}

inputFile content format:

Book Name,URL

## Api 
GET /books/?name

parameters:
name - name of the book
