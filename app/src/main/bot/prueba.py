import ccxt
import time
import os

api_key = os.getenv('API_KEY')
api_secret = os.getenv('API_SECRET')

exchange = ccxt.binance({
    'apiKey': api_key,
    'secret': api_secret,
})

def get_price(symbol):
    try:
        ticker = exchange.fetch_ticker(symbol)
        return ticker['last']
    except Exception as e:
        print(f"Se produjo un error al obtener el ticker: {e}")
        return None

def create_order(symbol, type, side, amount, price=None):
    try:
        order = exchange.create_order(symbol, type, side, amount, price)
        return order
    except Exception as e:
        print(f"Se produjo un error al crear la orden: {e}")
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
    symbol = 'BTC/USDT'
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
