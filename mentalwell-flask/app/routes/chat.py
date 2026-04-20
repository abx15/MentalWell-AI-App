from flask import Blueprint, request, jsonify
from flask_jwt_extended import jwt_required
import time
import logging
from app.services.gemini_service import chat as gemini_chat

logger = logging.getLogger(__name__)
chat_bp = Blueprint('chat', __name__)

@chat_bp.route('', methods=['POST'])
@jwt_required()
async def chat_endpoint():
    data = request.get_json()
    if not data or 'message' not in data:
        return jsonify({"error": "Missing 'message' field"}), 400
        
    user_message = data['message']
    conversation_history = data.get('conversation_history', [])
    user_id = data.get('user_id', 'unknown')
    
    logger.info(f"Chat request mapped -> user: {user_id} | History records: {len(conversation_history)}")
    
    try:
        response_text = await gemini_chat(user_message, conversation_history)
        
        return jsonify({
            "response": response_text,
            "timestamp": int(time.time() * 1000),
            "emotion_hint": "neutral"
        }), 200
        
    except Exception as e:
        logger.error(f"Error resolving chat inference: {e}")
        return jsonify({"error": "An error occurred generating response."}), 500
