import requests

url = "https://api.threatbook.com/v3/ip/query"

query = {
  "apikey":"698d51a19d8a121ce581499d7b701668",
  "resource":"159.203.93.255"
}

response = requests.request("GET", url, params=query)

print(response.json())
