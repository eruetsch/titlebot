# Titlebot project
TODO
- make pretty

# Steps to run the project:
- Clone project from github.
- Type `sbt run` in project folder. This will run the server
- Go to "http://localhost:9000/" in a browser.
- Type in urls, and have fun!

# Things I would have done differently
- better css (it's not my strongest suit)
- used jsoup or something similar instead of regex to parse site data
- made a shared class to include the jsonInfo and all its associated reading and writing functions
- make more resilient and informative: 
  - pop up error messages if url is invalid (does not include a .com/.edu/.org, etc)
  - tell user if there is no title or icon in page data instead of printing to console


