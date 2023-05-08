# Term Project: Syntax


## Project Details
cs logins: `hmasamur`, `abenjell`, `jwan8`, `dliu58` <br />
Total estimated time: ~48 hours <br />
[Github Repo](https://github.com/Syntax-App/syntax)  <br />

## Overview
Our term project Syntax is a typing-test website that helps users practice typing code syntax
instead of English. In addition, our site uses ChatGPT to provide explanations for every code
snippet generated from our custom graph-based weighted random-walk algorithm. User data
is stored using Firestore, and authentication is performed using email/password or Google
credentials through Firebase.

The project consists of a frontend using React and Typescript along with ChakraUI, and a backend
in Java.
The backend has a `Server` with endpoints regarding users (user/get to get user info + user/create
to create a new user) and races (race/start to generate snippets and explanations + race/end
to update user statistics). The backend also houses our algorithm, which is based on a Graph
of Nodes connected by Edges. Snippets are stored in JSON format in the backend as well, and are
kept track of using a proxy cache that uses a requester to call OpenAI's API for GPT explanations.

### File Structure
```
syntax
├── front
├── javaback/
│   ├── src/
│   │   ├── main/
│   │   │   └── java/
│   │   │       └── edu.brown.cs.student.main/
│   │   │           ├── algo/
│   │   │           │   ├── graph/
│   │   │           │       ├── Edge.java
│   │   │           │       ├── Graph.java
│   │   │           │       └── Node.java
│   │   │           │   └── snippets/
│   │   │           │       ├── GPTProxyCache.java
│   │   │           │       ├── GPTRequester.java
│   │   │           │       ├── JavaSnippets.json
│   │   │           │       ├── Snippets.java
│   │   │           │       └── TSXSnippets.json
│   │   │           └── server/
│   │   │               ├── config/
│   │   │               │   ├── APIKeysExample.java
│   │   │               │   └── FirebaseConfig.java
│   │   │               ├── handlers/
│   │   │               │   ├── race/
│   │   │               │   │   ├── EndHandler.java
│   │   │               │   │   └── StartHandler.java
│   │   │               │   └── user/
│   │   │               │       ├── UserCreateHandler.java
│   │   │               │       ├── UserGetHandler.java
│   │   │               │       └── UserRankHandler.java
│   │   │               ├── types/
│   │   │               │   ├── NewStats.java
│   │   │               │   ├── User.java
│   │   │               │   └── UserStats.java
│   │   │               ├── utils/
│   │   │               │   └── JSONUtils.java
│   │   │               ├── Server.java
│   │   │               ├── States.java
│   │   │               └── SerializeHelper.java
│   │   └── test/
│   │       └── java/
│   │           └── edu.brown.cs.student.main/
│   │               ├── mocks/
│   │                   ├── MockJSON.java
│   │                   └── MockStats.java
│   │               ├── TestSyntaxIntegration.java
│   │               └── TestSyntaxUnit.java
│   ├── pom.xml
│   ├── index.html
│   └── README.md                  
└── .gitignore
sprint-3-abenjell-hmasamur/
├── data/
│   ├── persons/
│   │   ├── names-invalid.csv
│   │   ├── names-no-header.csv
│   │   ├── names-part-invalid.csv
│   │   └── names-with-header.csv
│   ├── stars/
│   │   ├── star-part-invalid.csv
│   │   ├── stardata.csv
│   │   └── ten-star.csv
│   └── empty.csv
├── src/
│   ├── main/
│   │   └── java/
│   │       └── edu.brown.cs.student.main/
│   │           ├── csv/
│   │           │   ├── creators/
│   │           │   │   ├── CreatorFromRow.java
│   │           │   │   ├── FactoryFailureException.java
│   │           │   │   ├── ListCreator.java
│   │           │   │   ├── PersonCreator.java
│   │           │   │   └── StarCreator.java
│   │           │   ├── objectTypes/
│   │           │   │   ├── Person.java
│   │           │   │   └── Star.java
│   │           │   ├── Main.java
│   │           │   ├── Parser.java
│   │           │   └── Searcher.java
│   │           └── server/
│   │               ├── csvHandlers/
│   │               │   ├── LoadHandler.java
│   │               │   ├── SearchHandler.java
│   │               │   └── ViewHandler.java
│   │               ├── weather/
│   │               │   ├── CacheParams.java
│   │               │   ├── GeoCoord.java
│   │               │   ├── WeatherHandler.java
│   │               │   ├── WeatherProxy.java
│   │               │   ├── WeatherResponse.java
│   │               │   └── WeatherUtils.java
│   │               ├── Response.java
│   │               ├── Server.java
│   │               └── States.java
│   └── test/
│       └── java/
│           └── edu.brown.cs.student.main/
│               ├── mocks/
│               │   ├── MockJSON.java
│               │   └── MockWeatherUtils.java
│               ├── TestAPI.java
│               ├── TestAPIUtilities.java
│               ├── TestParser.java
│               └── TestSearcher.java
├── .gitignore
├── index.html
├── pom.xml
└── README.md
```

## Design Choices

- For all handlers of our API, we have created a class called `States`,
and instantiated one common instance that we passed in to the handlers.
This class keeps track of the Firebase/Firestore configuration as well as
each user's snippets.

- Each user's uuid, name, email, profile picture url, number of races, experience,
highest lines per minute, average lpm, highest accuracy, and average accuracy are
stored.

- We have created a `SerializeHelper` class, with a static method `helpSerialize` which, 
given a HashMap, takes care of serializing it into a JSON formatted `String`.

- `JSONUtils` in the `utils` packagehouses essential helper methods for getting 
and parsing data from/to JSONs. Its methods are called in the various handlers.

- `GPTProxyCache` wraps a `GPTRequester` class and adds on its functionality
to support caching. It makes use of the Google Guava library. Caching was crucial to save
time and money as new API calls to ChatGPT cause extended loading times and builds up financial
costs over time.

- `EndHandler` in the `race` package is called after every race, and updates the user's statistics
using calculated stats from the race they just completed.

- `StartHandler` in the `race` package is called before every race, and uses the algorithm to
keep track of a list of snippets for the user and responds with a snippet alongside an explanation
from the cache.

- `UserCreateHandler` in the `user` package is called whenever a user signs in to Syntax for the
first time. Their credentials and default data are stored in Firestore.

- `UserGetHandler` in the `user` package is called whenever an existing user's data needs to be
retrieved. The user's data is given as a response.

- `UserRankHandler` in the `user` package is called to update the leaderboard given all users and
their statistics. A sorted list based on highest lines per minute is given as a response.

- `NewStats`, `User`, and `UserStats` in the `types` package are used to facilitate storing and
updating user statistics in the various handlers.

- We use ChakraUI in the frontend for an aesthetically-pleasing and easy-to-use library of
  UI components to fit our specific needs.

- The algorithm prioritizes user learning by giving greater weight (probability) to snippets
  that have a lesser increase in difficulty. We also ensure that easier snippets are less likely
  to appear as a user's experience level increases.

- The frontend has a dark and light mode for accessibility.

## Testing

### Integration Tests
- Tests GET and POST requests
- Creating user data
- Ranking users
- Getting user data
- Invalid inputs for user handlers

### Unit Tests
- Fuzz testing for weight calculation
- Standard weight calculation
- Mock requesting
- Serialization
- Weight swapping
- Caching (size, expiration)

## Errors/Bugs
There are no known bugs.

## How to...
### Run Tests
Our backend tests are traditional JUnit tests and can be run using the Play button in IntelliJ!

### Run the Program
The program can be run by clicking the play button in the `Server.java` file,
or by compiling it and running it with the `java` Shell command.
You can run `npm run dev` in `front` while the server is running to access the site.