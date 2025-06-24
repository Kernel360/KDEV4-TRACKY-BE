cd ../tracky-consumer
./gradlew build -x test

# `` 빼고 입력
docker build -t `레지스트리 경로` -f Dockerfile --push .

# ``는 빼고 입력
ssh -i "ppk 키 경로" tojaman@`서버 ip` 'sudo docker pull `레지스트리 경로`'
ssh -i "ppk 키 경로" tojaman@`서버 ip` 'sudo docker pull `레지스트리 경로`'
ssh -i "ppk 키 경로" tojaman@`서버 ip` 'sudo docker pull `레지스트리 경로`'