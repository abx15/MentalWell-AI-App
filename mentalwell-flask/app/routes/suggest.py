from flask import Blueprint, request, jsonify
from flask_jwt_extended import jwt_required
import logging

logger = logging.getLogger(__name__)
suggest_bp = Blueprint('suggest', __name__)

@suggest_bp.route('', methods=['POST'])
@jwt_required()
def suggest_endpoint():
    data = request.get_json()
    if not data or 'mood' not in data:
        return jsonify({"error": "Missing parameter 'mood'"}), 400
        
    mood = data['mood'].lower()
    
    suggestions = []
    
    if mood in ['sad', 'anxious']:
        suggestions = [
            {"title": "Deep Breathing", "description": "4-7-8 breathing technique down-regulates the nervous system.", "duration": "5 mins", "type": "Relaxation"},
            {"title": "Mindful Meditation", "description": "A guided body scan to ground your thoughts.", "duration": "10 mins", "type": "Meditation"},
            {"title": "Light Walk", "description": "A brief walk outside to get fresh air and change scenery.", "duration": "15 mins", "type": "Activity"}
        ]
    elif mood == 'angry':
        suggestions = [
            {"title": "Expressive Journaling", "description": "Write down what's bothering you to release tension.", "duration": "10 mins", "type": "Journaling"},
            {"title": "Cold Water Splash", "description": "Splash cold water on your face to trigger the diving reflex.", "duration": "2 mins", "type": "Physical"},
            {"title": "Calming Music", "description": "Listen to a curated playlist to soothe frustration.", "duration": "15 mins", "type": "Audio"}
        ]
    elif mood == 'happy':
        suggestions = [
            {"title": "Gratitude Journal", "description": "Capture this moment and note what made it special.", "duration": "5 mins", "type": "Journaling"},
            {"title": "Share the Joy", "description": "Call or message a close friend to share positivity.", "duration": "10 mins", "type": "Social"},
            {"title": "Creative Activity", "description": "Channel your positive energy into something creative.", "duration": "20 mins", "type": "Creative"}
        ]
    else:
        suggestions = [
            {"title": "Mindful Check-in", "description": "Take a moment to sit and observe your feelings logically.", "duration": "5 mins", "type": "Meditation"}
        ]
        
    return jsonify({"suggestions": suggestions}), 200
