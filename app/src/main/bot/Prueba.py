import time
import http.client
import json

api_key = "BQYhfZlL6HQUffO4vOhyj6MfNJEwISrd"

def get_price(symbol):
    try:
        conn = http.client.HTTPSConnection("graphql.bitquery.io")
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
        headers = { 'Content-Type': 'application/json' }
        conn.request("POST", "/chain-gateway/mainnet", json.dumps({'query': query}), headers)
        res = conn.getresponse()
        data = res.read()
        price = json.loads(data)['data']['ethereum']['dexTrades'][0]['close_price']
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
    print(f"Notificar al usuario: {message}")

def trading_bot():
    symbol = 'ETH/USDT'
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

