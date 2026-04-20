import os
from dotenv import load_dotenv

load_dotenv()

class Config:
    GEMINI_API_KEY = os.environ.get('GEMINI_API_KEY')
    JWT_SECRET_KEY = os.environ.get('JWT_SECRET_KEY', 'default-unsafe-secret')
    CORS_ALLOWED_ORIGINS = ["*"] # Consider securing in actual deployment
    RATELIMIT_DEFAULT = "60 per minute"

class DevelopmentConfig(Config):
    DEBUG = True

class ProductionConfig(Config):
    DEBUG = False

config = {
    'development': DevelopmentConfig,
    'production': ProductionConfig,
    'default': DevelopmentConfig
}
