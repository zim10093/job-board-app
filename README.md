# job board app
### Description
This is an application where you have more possibility to work with job vacancies than with a given API. You can get statistics, a number of jobs by location, top jobs and work with jobs with pagination and sorting. This app synchronizes every hour at 25 minutes. The time of sync depends on how many new jobs exist in the external API. The delay between every 100 jobs is 10 sec to avoid a heavy load on the external server.

### Endpoints:
 - `/jobs` returns all job vacancies with pagination and sort
 - `/jobs/statistic` returns numbers of vacancies by location
 - `/jobs/top{limit}` returns top job vacancies by views limited by path variable
 - `jobs/{slug}` returns job vacancies with description

#### SortBy parameters:
- slug
- company
- title
- description
- remote
- url
- location
- createdAt
- views

#### Direction parameter:
- :DESC
- :ASC

There you can set several parameters for sorting.
Example of sort and direction parameters:

`/jobs?count=10&page=1&sortBy=slug:ASC;createdAt:DESC;remote`

#### All data sync with external api by url every hour at 25 minute

`https://www.arbeitnow.com/api/job-board-api`

#### Used technology:
- Java 11
- Spring boot 2.7
- H2 DB
- JUnit 5.9
- Mockito 4.7
- SOLID
- REST
- N-layer architecture
  - Controller
  - Service
  - Persistence
