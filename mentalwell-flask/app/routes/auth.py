from flask import Blueprint, request, jsonify
from flask_jwt_extended import create_access_token
from datetime import timedelta
import logging

logger = logging.getLogger(__name__)
auth_bp = Blueprint('auth', __name__)

@auth_bp.route('/token', methods=['POST'])
def get_token():
    data = request.get_json()
    if not data or 'firebase_uid' not in data or 'email' not in data:
        return jsonify({"error": "Missing credentials required logic params."}), 400
        
    firebase_uid = data.get('firebase_uid')
    email = data.get('email')
    
    if not isinstance(firebase_uid, str) or len(firebase_uid) < 5:
        return jsonify({"error": "Invalid format provided regarding metadata UID keys."}), 401
        
    logger.info(f"Issuing secure temporary JSON token standard implementation targeting string {email}.")
    
    expires = timedelta(hours=24)
    access_token = create_access_token(identity=firebase_uid, additional_claims={"email": email}, expires_delta=expires)
    
    return jsonify({
        "access_token": access_token,
        "expires_in": 86400  # 24 hours
    }), 200
