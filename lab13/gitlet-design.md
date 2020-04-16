# Gitlet Design Document

**Name**: Diraj Thajali

## Classes and Data Structures
### Commit

This class will consist of references to its two parents, the committed blobs, ID, timeStamp, head pointer and its branch name. 

**Fields**

1. Commit parent; secondParent;
2. Blobs[] contents;
3. int commitID;
4. Date timeStamp;
5. Branch head;
6. Branch name;
7. @overide hashcode()
### Blob

This class represents a file. It has its own name, ID and content. 

**Fields**

1. String name;
2. int blobID;
3. File content;
### Branch

This class represents a branch. It is just a pointer to an instance of commit. 

1. String name;
2. Commit commit;
### Gitlet

This class handles all the commands of Gitlet. 

**Fields**

1. Branch head - current branch
2. Blob[ ] dataInStaging - array of blobs
3. Commit[ ] dataInHistory - array of commits
4. File STAGING_AREA - a file where we can read and write serializable blobs.
5. File HISTORY - a file where we can read and write serializable commits. 
6. processCommands()
----------
## Algorithms
### Commit class
1. Commit() - the class constructor. It takes in Array of Blobs and creates a commit instance. 


### Blob class
1. Blob() - the class constructor. It takes in a File and a fileName and creates a blob instance. 
### Branch
1. Branch() - the class constructor. 
### Gitlet class
1. Gitlet() - the class constructor
2. init() - initialize all the files and branch (STAGING_AREA, HISTORY, BRANCH head)we need to save and keep track of Blobs and Commits. 
3. add() - add the file to dataInStaging and write to STAGING_AREA. 
4. commit() - create an instance of commit using dataInStaging and add it to dataInHistory and empty the dataInStaging. Point the head to this branch. 
5. remove() - remove from dataInSharing.
6. showLog() - read and list all the commits from head to the first commit from HISTORY. 
7. showGlobalLog() - read and list all the commits from HISTORY. 
8. find() - print out ids of all commits that have the given commit message. 
9. status() - display what branches currently exists (* on the current branch) and also the files in STAGING_AREA
10. checkOut() - read from HISTORY and move the head to the given file or given id or given branch in dataInHistory. 
11. createBranch() - create a new instance of branch and point it to the given commit. 
12. removeBranch() - remove the branch with given name
13. reset() - read from HISTORY add all the blobs tracked by the given commit. Remove all the blobs that are not present in that blobs. 
14. merge()
    - check if a merge is necessary
        - if it is 
            - check if the blobs conflict
                - if they do: 
                    - mergeConflict() - merges blobs that conflict by comparing it to its latest common ancestor.
                - else:
                    - simpleMerge() - merge blobs by comparing it to its latest common ancestor. 
        - else 
            - do nothing


----------
## Persistance

In order to persist changes in my .gitlet directory, I plan to do the following: 

1. **When initializing, create two new files inside .gitlet**
    - staging area and history of commits. 
    - To store the commits, I plan to use Hash Table (Array of Commits) with load factor 5 and resizing by doubling the size when the array is full. 
    - Similarly, to store the files in staging area, I plan to use Array of Blobs. 
2. **Save the Blobs and Commits using writeObject() method from the Utils class.** 
    - Write the Array of Blobs to the file, staging area, and Hash Table to the file, history of commits, using writeContents in the Utils class provided.  
3. **Retrieve using readObject() method from the Utils class.** 

