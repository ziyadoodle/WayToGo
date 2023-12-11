FROM python:3.11.1

WORKDIR /workspace

COPY requirements.txt requirements.txt

RUN pip install -r requirements.txt

COPY . .

ENV PYTHONUNBUFFERED=1

ENV HOST 0.0.0.0

EXPOSE 8080
# or 8000(port main.py)

CMD ["python", "main.py"]