# job board api

### Endpoints:
 - `/jobs` all job vacancies with pagination and sort
 - `/jobs/statistic` returned numbers of vacancies by location
 -  `/jobs/top10` returned top 10 last vacancies

#### SortBy parameters:
- slug
- company
- title
- description
- remote
- url
- location
- createdAt
#### Direction parameter:
- :DESC
- :ASC

There you can set several parameters for sorting.
Example of sort and direction parameters:

`?count=10&page=1&sortBy=slug:ASC;createdAt:DESC;remote`

#### All data sync with external api by url every hour at 25 minute

`https://www.arbeitnow.com/api/job-board-api`

#### Used technology:
- Java 11
- Spring boot 2.7
- H2 BD
- JUnit 5.9
- Mockito 4.7