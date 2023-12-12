FROM python:3.11

WORKDIR /workspace

COPY requirements.txt requirements.txt

RUN pip install -r requirements.txt

COPY . .

ENV PYTHONUNBUFFERED=1

ENV HOST=localhost

EXPOSE 3030

CMD exec uvicorn main:app --host 0.0.0.0 --port 3030