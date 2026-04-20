from flask import Flask, jsonify
from flask_cors import CORS
from flask_jwt_extended import JWTManager
from config import config
import logging

def create_app(config_name='default'):
    app = Flask(__name__)
    app.config.from_object(config[config_name])

    CORS(app, resources={r"/api/*": {"origins": app.config['CORS_ALLOWED_ORIGINS']}})
    
    JWTManager(app)

    logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')

    # Register Blueprints
    from app.routes.chat import chat_bp
    from app.routes.analyze import analyze_bp
    from app.routes.suggest import suggest_bp
    from app.routes.auth import auth_bp

    app.register_blueprint(chat_bp, url_prefix='/api/chat')
    app.register_blueprint(analyze_bp, url_prefix='/api/analyze')
    app.register_blueprint(suggest_bp, url_prefix='/api/suggest')
    app.register_blueprint(auth_bp, url_prefix='/api/auth')

    @app.route('/health', methods=['GET'])
    def health_check():
        return jsonify({"status": "healthy"}), 200

    return app
