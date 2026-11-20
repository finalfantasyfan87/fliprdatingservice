![Screenshot 2025-06-03 035209](https://github.com/user-attachments/assets/474a96cb-36c4-47da-9fc9-92039607dcc2)
![Screenshot 2025-06-03 033430](https://github.com/user-attachments/assets/0d4c85cb-1175-4cb8-8866-9860cc18b78a)

![alt text](<Screenshot 2026-11-19 at 7.11.15 PM.png>)


## Running locally

To enable MongoDB, uncomment the connection URI in `src/main/resources/application.yml` and set the `MONGODB_URI` environment variable. Keep credentials out of source control. The API listens on port `9090` by default.

curl --location 'http://localhost:9090/userapi/compatibility?first=cancer&second=pisces'

curl --location 'http://localhost:9090/userapi/users'
