from flask import Blueprint, request, jsonify
from flask_jwt_extended import jwt_required
import logging
from app.services.emotion_service import analyze_emotion

logger = logging.getLogger(__name__)
analyze_bp = Blueprint('analyze', __name__)

@analyze_bp.route('', methods=['POST'])
@jwt_required()
async def analyze_endpoint():
    data = request.get_json()
    if not data or 'text' not in data:
        return jsonify({"error": "Missing 'text' parameter."}), 400
        
    text = data['text']
    user_id = data.get('user_id', 'unknown')
    
    logger.info(f"Analyzing prompt request mapping for user: {user_id}")
    
    try:
        result = await analyze_emotion(text)
        
        mood_scores = {
            "happy": 9, "hopeful": 8, "neutral": 5, 
            "sad": 3, "anxious": 3, "stressed": 2, "angry": 1
        }
        mapped_emotion = result.get('emotion', 'neutral').lower()
        score = mood_scores.get(mapped_emotion, 5)
        
        return jsonify({
            "emotion": result.get('emotion', 'neutral'),
            "confidence": result.get('confidence', 0.0),
            "mood_score": score,
            "suggestions": result.get('suggestions', [])
        }), 200
        
    except Exception as e:
        logger.error(f"Inference error mapping: {e}")
        return jsonify({"error": "Failed executing request analyzing logic endpoint."}), 500
