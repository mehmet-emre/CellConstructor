This project is based on **Organic Builder** (which is published under GNU General Public License) and extended only for academic purposes. 
To see the original source code and author, please visit [here](https://github.com/BertrandDechoux/OrganicBuilder/).

**Compile and run the tests**
```
mvn clean test
```

**Create the executable jar**
```
mvn clean package
ls target/*-jar-with-dependencies.jar
```

**Create the artefact site**
```
mvn clean site
```
With depedencies, javadocs, test coverage and code/style checks (checkstyle,pmd,findbugs)
