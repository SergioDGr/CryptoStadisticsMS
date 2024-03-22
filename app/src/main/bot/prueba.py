import ccxt
import time
import os
import requests

api_key = os.getenv('API_KEY')
api_secret = os.getenv('API_SECRET')

def get_price(symbol):
    try:
        url = f"https://graphql.bitquery.io/chain-gateway/mainnet"
        query = """
        {
            ethereum(network: ethereum) {
                dexTrades(options: {limit: 1, asc: "timeInterval.minute"}, date: {since: "2022-01-01"}, baseCurrency: {is: "%s"}) {
                    timeInterval {
                        minute(count: 15)
                    }
                    baseCurrency {
                        symbol
                    }
                    quoteCurrency {
                        symbol
                    }
                    baseAmount
                    quoteAmount
                    trades: count
                    quotePrice
                    maximum_price: quotePrice(calculate: maximum)
                    minimum_price: quotePrice(calculate: minimum)
                    open_price: minimum(of: block, get: quote_price)
                    close_price: maximum(of: block, get: quote_price)
                }
            }
        }
        """ % symbol
        response = requests.post(url, json={'query': query})
        data = response.json()
        price = data['data']['ethereum']['dexTrades'][0]['close_price']
        return price
    except Exception as e:
        print(f"Se produjo un error al obtener el precio: {e}")
        return None

def calculate_sma(data, window):
    if len(data) >= window:
        return sum(data[-window:]) / window
    else:
        return None

def notify_user(message):
    # Lógica para enviar notificaciones al usuario en Kotlin
    # Puedes implementar esto en tu aplicación móvil
    # Por ejemplo, usando Firebase Cloud Messaging (FCM)
    print(f"Notificar al usuario: {message}")

def trading_bot():
    symbol = 'ETH/USDT'  # Cambia el símbolo según las convenciones de Bitquery
    sma_window = 20
    trade_amount = 0.001
    data = []

    while True:
        price = get_price(symbol)
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

        time.sleep(1)

trading_bot()
