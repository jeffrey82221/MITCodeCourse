# MITCodeCourse

This project contain the assignment implementation of MIT OCW course: MIT6.031 (http://web.mit.edu/6.031/www/fa18/), 
- The propose is only for code review among a code-practicing team in Taiwan.  


Each folder contains a problem set from the course, as shown below: 

Problem Sets (These are personal programming projects)


- 0: Turtle Graphics (https://ocw.mit.edu/ans7870/6/6.005/s16/psets/ps0/)
- 1: Tweet Tweet (https://ocw.mit.edu/ans7870/6/6.005/s16/psets/ps1/)
- 2: Poetic Walks (https://ocw.mit.edu/ans7870/6/6.005/s16/psets/ps2/)
- 3: Expressivo (https://ocw.mit.edu/ans7870/6/6.005/s16/psets/ps3/)
- 4: Multiplayer Minesweeper (https://ocw.mit.edu/ans7870/6/6.005/s16/psets/ps4/)

-  Team based project: ABC Music Player(https://ocw.mit.edu/ans7870/6/6.005/s16/projects/abcplayer/) 


Interesting coders can check the belowing list for Reading material: 

For Problem Set 0: 

- 01: Static Checking (http://web.mit.edu/6.031/www/sp18/classes/01-static-checking/)
- 02: Basic Java (http://web.mit.edu/6.031/www/sp18/classes/02-basic-java/)
- 03: Testing (http://web.mit.edu/6.031/www/sp18/classes/03-testing/)
- 04: Code Review (http://web.mit.edu/6.031/www/sp18/classes/04-code-review/)
- 05: Version Control (http://web.mit.edu/6.031/www/sp18/classes/05-version-control/)
- 06: Specifications (http://web.mit.edu/6.031/www/sp18/classes/06-specifications/)


For Problem Set 1: 

- 07: Designing Specifications (http://web.mit.edu/6.031/www/sp18/classes/07-designing-specs/)
- 08: Mutability & Immutability (http://web.mit.edu/6.031/www/sp18/classes/08-immutability/)
- 09: Avoiding Debugging (http://web.mit.edu/6.031/www/sp18/classes/09-avoiding-debugging/)
- 10: Abstract Data Types (http://web.mit.edu/6.031/www/sp18/classes/10-abstract-data-types/)
- 11: Abstraction Functions & Rep Invariants (http://web.mit.edu/6.031/www/sp18/classes/11-abstraction-functions-rep-invariants/)
- 12: Interfaces & Enumerations (http://web.mit.edu/6.031/www/sp18/classes/12-interfaces-enums/)

For Problem Set 2: 

- 13: Debugging (http://web.mit.edu/6.031/www/sp18/classes/13-debugging/)
- 14: Recursion (http://web.mit.edu/6.031/www/sp18/classes/14-recursion/)
- 15: Equality (http://web.mit.edu/6.031/www/sp18/classes/15-equality/)
- 16: Recursive Data Types (http://web.mit.edu/6.031/www/sp18/classes/16-recursive-data-types/)
- 17: Regular Expressions & Grammars (http://web.mit.edu/6.031/www/sp18/classes/17-regex-grammars/)
- 18: Parsers (http://web.mit.edu/6.031/www/sp18/classes/18-parsers/) 

For Problem Set 3: 

- 19: Concurrency (http://web.mit.edu/6.031/www/sp18/classes/19-concurrency/)
- 20: Thread Safety (http://web.mit.edu/6.031/www/sp18/classes/20-thread-safety/)
- 21: Locks & Synchronization (http://web.mit.edu/6.031/www/sp18/classes/21-locks/)
- 22: Queues & Message-Passing (http://web.mit.edu/6.031/www/sp18/classes/22-queues/)
- 23: Sockets & Networking (http://web.mit.edu/6.031/www/sp18/classes/23-sockets-networking/)
- 24: Callbacks (http://web.mit.edu/6.031/www/sp18/classes/24-callbacks/)

For Problem Set 4: 

- 25: Map, Filter, Reduce (http://web.mit.edu/6.031/www/sp18/classes/25-map-filter-reduce/)
- 26: Little Languages I (http://web.mit.edu/6.031/www/sp18/classes/26-little-languages-1/)
- 27: Little Languages II (http://web.mit.edu/6.031/www/sp18/classes/27-little-languages-2/)
- 28: Team Version Control (http://web.mit.edu/6.031/www/sp18/classes/28-team-version-control/)

# Note:  
How to push a tag for alpha or beta version of code release? 

`
git tag -a ps0-alpha -m 'problem set alpha version'
`

`
git push --tags 
`

How to manage branches for new problem set ? 
`
git checkout master      
`
// go back to the master branch. 
`
git checkout --orphan ps1  
`
// create a branch, namely "ps[n]", for problem set n.  
`
rm .git/index              
`
// remove previous git history from this branch to make it independent of the master branch 
`
rm -r *                    
`
// clean up this branch by removing all files. 
// Then, do: download ps1 project file from course webside  
// Finally, finish the add-commit-push cycle as below: 
`
git add .
`
`
git commit -m "problem set 1 initialized" 
`
`
git push --set-upstream origin ps1
`


