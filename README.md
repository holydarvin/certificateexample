# Пример метрики для мониторинга оставшегося срока сертификата


## Для запуска 
- git clone https://github.com/holydarvin/certificateexample
- cd certificateexample
- docker build -t metr . 
- docker run -it --rm -p 8080:8080 metr 
