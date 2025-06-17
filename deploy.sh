#!/bin/bash

cd ~/portfolio || exit

echo "🔁 Git Pull"
git pull origin main || exit

echo "🛠️ Building JAR"
./gradlew build || exit

echo "🧹 Restarting PM2"
pm2 delete portfolio
pm2 start "java -jar build/libs/portfolio-0.0.1-SNAPSHOT.jar" --name portfolio

echo "✅ Deploy Done"
