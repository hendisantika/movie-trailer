#!/bin/bash
ssh -p "${SERVER_PORT}" "${SERVER_USERNAME}"@"${SERVER_HOST}" -i key.txt -t -t -o StrictHostKeyChecking=no << 'ENDSSH'
cd ~/movie-service
cat .env
set +a
source .env
start=$(date +"%s")
aws ecr get-login-password --region $AWS_REGION | docker login --username AWS --password-stdin $AWS_ECR_REGISTRY
docker pull $AWS_ECR_REPOSITORY:$IMAGE_TAG

if [ "$(docker ps -qa -f name=$CONTAINER_NAME)" ]; then
    if [ "$(docker ps -q -f name=$CONTAINER_NAME)" ]; then
        echo "Container is running -> stopping it..."
        docker system prune -af
        docker stop $CONTAINER_NAME;
        docker rm $CONTAINER_NAME
    fi
fi

docker run -d --restart unless-stopped -p $APP_PORT:$APP_PORT --env-file .env --name $CONTAINER_NAME  -e SPRING_PROFILES_ACTIVE=github $AWS_ECR_REPOSITORY:$IMAGE_TAG
docker ps
exit
ENDSSH

if [ $? -eq 0 ]; then
  exit 0
else
  exit 1
fi

end=$(date +"%s")

diff=$(($end - $start))

echo "Deployed in : ${diff}s"