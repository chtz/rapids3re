# rapids3re samples

Pre condition: AWS IAM user with S3 ~full access

## Create bucket

```
java -jar target/rapids3re-0.0.1.jar --command=createBucket --bucket=chtztest20170310e --expirationInDays=1 --accessKey=$ACCESS_KEY_ID --secretKey=$SECRECT_KEY
```

## Upload file

```
PRESIGNED_URL=$(java -jar target/rapids3re-0.0.1.jar --command=putObject --file=target/rapids3re-0.0.1.jar --bucket=chtztest20170310e --expirationDurationMinutes=10 --accessKey=$ACCESS_KEY_ID --secretKey=$SECRECT_KEY)
```

## Download file

```
wget "$PRESIGNED_URL" -O downloaded.jar
```

## Delete bucket

```
java -jar target/rapids3re-0.0.1.jar --command=deleteBucket --bucket=chtztest20170310e --accessKey=$ACCESS_KEY_ID --secretKey=$SECRECT_KEY
```
