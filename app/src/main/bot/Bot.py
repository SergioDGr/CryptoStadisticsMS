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
              options: { desc: ["block.height", "transaction.index"], limit: 20 }
              date: { since: "2024-04-26T15:00:00.000Z" }
            ) {
              block {
                height
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
            quote_price20 = []
            for i in range(0, 20):
                quote_price = data["data"]["ethereum"]["dexTrades"][i]["quotePrice"]
                quote_price20.append(quote_price)
                print(f"El precio de la cripto es: {quote_price}")
            return quote_price20
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

def ema(data, alpha, lookback, what, where):
    ema_values = data[what].ewm(alpha=alpha, adjust=False, span=lookback).mean()
    data[where] = ema_values
    return data[where]

def calculate_rsi(data, window=14):
    # Calcula los cambios de precio
    data["price_change"] = data["quote_price"].diff()

    # Calcula las ganancias y pérdidas
    gains = data["price_change"].apply(lambda x: max(x, 0))
    losses = data["price_change"].apply(lambda x: max(-x, 0))

    # Calcula las medias móviles exponenciales de ganancias y pérdidas
    avg_gain = ema(gains, alpha=2 / (window + 1), lookback=window, what="gains", where="avg_gain")
    avg_loss = ema(losses, alpha=2 / (window + 1), lookback=window, what="losses", where="avg_loss")

    # Calcula el índice de fuerza relativa (RSI)
    rs = avg_gain / avg_loss
    data["rsi"] = 100 - (100 / (1 + rs))

    return data

def calculate_macd(data, short_window=12, long_window=26, signal_window=9):
    # Calcula las medias móviles exponenciales (EMA)
    ema_short = ema(data, alpha=2 / (short_window + 1), lookback=short_window, what="quote_price", where="short_ema")
    ema_long = ema(data, alpha=2 / (long_window + 1), lookback=long_window, what="quote_price", where="long_ema")

    # Calcula la diferencia entre las EMAs
    data["macd"] = ema_short - ema_long

    # Calcula la señal MACD (EMA de la diferencia)
    data["macd_signal"] = ema(data, alpha=2 / (signal_window + 1), lookback=signal_window, what="macd", where="macd_signal")

    return data

def notify_user(message):
    print(f"Notificar al usuario: {message}")

def trading_bot():
    #Comprar=1, Vender=2
    signal1 = 0
    signal2 = 0
    signal3 = 0

    #Signal1:
    #symbol = 'ETH/USDT'
    sma_window = 20
    #trade_amount = 0.001
    data = get_price()
    price = data[0]
    data.append(price)
    sma = calculate_sma(data, sma_window)

    if sma is not None:
        print(f'Último precio: {price}, SMA: {sma:.2f}')

        if price > sma:
            notify_user("¡Hora de comprar!")
            signal1 = 1
        elif price < sma:
            notify_user("¡Hora de vender!")
            signal1 = 2

    # Calcula el RSI y el MACD
    '''
    data = calculate_rsi(data)
    data = calculate_macd(data)

    # Define umbrales para señales
    rsi_buy_threshold = 30
    rsi_sell_threshold = 70
    macd_buy_threshold = 0
    macd_sell_threshold = 0

    # Comprueba las señales
    for i in range(len(data)):
        if data["rsi"][i] < rsi_buy_threshold:
            notify_user("¡Hora de comprar2!")
            signal2 = 1
        elif data["rsi"][i] > rsi_sell_threshold:
            notify_user("¡Hora de vender2!")
            signal2 = 2

        if data["macd"][i] > macd_buy_threshold:
            notify_user("¡Hora de comprar3!")
            signal3 = 1
        elif data["macd"][i] < macd_sell_threshold:
            notify_user("¡Hora de vender3!")
            signal3 = 2
    if signal1 == 1 and signal2 == 1 and signal3 == 1:
        notify_user("¡Hora de comprar TODO!")
    if signal1 == 2 and signal2 == 2 and signal3 == 2:
        notify_user("¡Hora de vender TODO!")
    '''
