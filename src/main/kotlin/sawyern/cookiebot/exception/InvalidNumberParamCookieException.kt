package sawyern.cookiebot.exception

class InvalidNumberParamCookieException(arg: String): CookieException("Invalid number parameter: Expected Int, got $arg")