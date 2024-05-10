import time
import http.client
import json

api_key = "BQYhfZlL6HQUffO4vOhyj6MfNJEwISrd"

def get_price():
    try:
        query = """
        {
          ethereum(network: bsc) {
            dexTrades(
              baseCurrency: { is: "0x2170ed0880ac9a755fd29b2688956bd959f933f8" }
              quoteCurrency: { is: "0x8AC76a51cc950d9822D68b83fE1Ad97B32Cd580d" }
              options: { desc: ["block.height", "transaction.index"], limit: 1 }
              date: { since: "2024-04-26T15:00:00.000Z" }
            ) {
              block {
                height
                timestamp {
                  time(format: "%Y-%m-%d %H:%M:%S")
                }
              }
              transaction {
                index
              }
              quotePrice
            }
          }
        }
        """

        # Configura tus encabezados (reemplaza 'API-KEY' con tu clave de API)
        headers = {'Content-Type': 'application/json', "X-API-KEY": api_key}

        # Realiza la solicitud POST a la API de Bitquery
        conn = http.client.HTTPSConnection("graphql.bitquery.io")
        conn.request("POST", "/", json.dumps({"query": query}), headers)
        response = conn.getresponse()

        if response.status == 200:
            data = json.loads(response.read())
            quote_price = data["data"]["ethereum"]["dexTrades"][0]["quotePrice"]
            print(f"El precio de la cripto es: {quote_price}")
            return quote_price
        else:
            print("Ocurrió algo inesperado al hacer la solicitud.")
            return None
    except Exception as e:
        print(f"Se produjo un error al obtener el precio: {e}")
        return None


def calculate_sma(data, window):
    if len(data) >= window:
        return sum(data[-window:]) / window
    else:
        return None

def notify_user(message):
    print(f"Notificar al usuario: {message}")

def trading_bot():
    symbol = 'ETH/USDT'
    sma_window = 20
    trade_amount = 0.001
    data = []
    for i in range(1, 21):
        price = get_price()
        if price is None:
            continue
        data.append(price)
        sma = calculate_sma(data, sma_window)

        if sma is not None:
            print(f'Último precio: {price}, SMA: {sma:.2f}')

            if price > sma:
                notify_user("¡Hora de comprar!")
            elif price < sma:
                notify_user("¡Hora de vender!")
        time.sleep(60)

