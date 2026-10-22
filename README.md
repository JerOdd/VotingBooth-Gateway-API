# VotingBooth-Gateway

API Gateway for VotingBooth: the single entry point that authenticates requests (JWT) and routes them to the backend services.

## Running the whole backend locally

`docker-compose.yml` builds and starts the gateway, auth-api, the mobile BFF and the internal services
(vote, law, representative) from the sibling checkouts in `../`. The law and representative jobs run once at
startup to fill their databases from the Assemblée nationale open data (about a minute), then exit;
`docker compose up law-job representative-job` syncs again.

auth-api signs tokens with an RSA key that you generate once into a git-ignored `.env`:

```sh
echo "JWT_PRIVATE_KEY=$(openssl genpkey -algorithm RSA -pkeyopt rsa_keygen_bits:2048 | grep -v -- ----- | tr -d '\n')" > .env
docker compose up --build
```

Mobile clients then call the gateway at http://localhost:8081/mobile/v1/...
