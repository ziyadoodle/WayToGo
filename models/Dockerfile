FROM tiangolo/uvicorn-gunicorn-fastapi:python3.11

WORKDIR /workspace

COPY requirements.txt requirements.txt

RUN pip install -r requirements.txt

COPY . .

ENV PYTHONUNBUFFERED=1

ENV NAME World

EXPOSE 8080

CMD ["uvicorn", "main:app"]