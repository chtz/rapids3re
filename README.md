# rapids3re samples

Share files via s3 short-living presigned download URLs.

[ ![Codeship Status for chtz/PoormanS3Tool](https://codeship.com/projects/2b166ec0-e808-0134-c58f-1635a52dc88d/status?branch=master)](https://codeship.com/projects/207318)

Download [rapids3re-0.0.1.jar](https://s3-eu-west-1.amazonaws.com/www.opensource.p.iraten.ch/rapids3re-0.0.1.jar) (built by Codeship)

Pre condition: AWS IAM user with S3 ~full access.

## Create bucket

```
java -jar target/rapids3re-0.0.1.jar --command createBucket --bucket $BUCKET --expirationInDays 1 --region $REGION --accessKey $ACCESS_KEY_ID --secretKey $SECRECT_KEY
```

## Upload file

```
PRESIGNED_URL=$(java -jar target/rapids3re-0.0.1.jar --command putObject --file $FILE --bucket $BUCKET --expirationDurationMinutes 60 --region $REGION --accessKey $ACCESS_KEY_ID --secretKey $SECRECT_KEY)
```

## Download file

```
curl $PRESIGNED_URL
```

## Delete bucket

```
java -jar target/rapids3re-0.0.1.jar --command deleteBucket --bucket $BUCKET --region $REGION --accessKey $ACCESS_KEY_ID --secretKey $SECRECT_KEY
```
