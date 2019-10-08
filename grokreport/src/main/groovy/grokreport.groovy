// cerner_2^5_2019

package grokreport

import groovyx.net.http.HTTPBuilder
import static groovyx.net.http.ContentType.TEXT

void getSearchResult(grokLink, fullSearch, projects){
    def client = new HTTPBuilder(grokLink+'/api/v1/search?projects='+projects+'&full='+fullSearch)
    client.setHeaders(Accept: 'application/json')
    
    def grokOutput = client.get(contentType: TEXT)
    def slurper = new groovy.json.JsonSlurper().parse(grokOutput)
    totalResults = slurper.resultCount
    println "Total results: ${totalResults}"
    File outfile = new File(fullSearch+'.csv')
    String outcontent = 'Filename,Count\n'
    for (def key: slurper.results.keySet()){
        outcontent+="${key},${slurper.results."${key}".size()}\n"
    }
    outcontent = outcontent.substring(0, outcontent.length() - 1)
    outfile.write(outcontent)
}

print "what is the grok link? "
def grokLink = System.in.newReader().readLine()

print "what projects (, separated) do you want to look into? "
List<String> projectsList = System.in.newReader().readLine().split(',')
String projects = projectsList[0]
for (int i=1;i<projectsList.size();i++){
    projects+="&projects=${projectsList[i]}"
}

print "what are you searching for? "
def fullSearch = System.in.newReader().readLine()

getSearchResult(grokLink, fullSearch, projects)