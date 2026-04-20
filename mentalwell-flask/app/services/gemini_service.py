import os
import logging
import google.generativeai as genai
from google.generativeai.types import HarmCategory, HarmBlockThreshold

logger = logging.getLogger(__name__)

SYSTEM_PROMPT = """
You are MentalWell, a compassionate AI mental health companion. Respond with empathy and warmth. Keep responses concise (max 150 words). Never give medical diagnoses. Always encourage professional help for serious issues. Detect emotional tone and respond accordingly.
"""

def setup_gemini():
    api_key = os.environ.get('GEMINI_API_KEY')
    if not api_key:
        logger.error("GEMINI_API_KEY is not set.")
        return None
    genai.configure(api_key=api_key)
    return genai.GenerativeModel(
        model_name='gemini-1.5-flash',
        system_instruction=SYSTEM_PROMPT,
        safety_settings={
            HarmCategory.HARM_CATEGORY_HARASSMENT: HarmBlockThreshold.BLOCK_MEDIUM_AND_ABOVE,
            HarmCategory.HARM_CATEGORY_HATE_SPEECH: HarmBlockThreshold.BLOCK_MEDIUM_AND_ABOVE,
            HarmCategory.HARM_CATEGORY_SEXUALLY_EXPLICIT: HarmBlockThreshold.BLOCK_MEDIUM_AND_ABOVE,
            HarmCategory.HARM_CATEGORY_DANGEROUS_CONTENT: HarmBlockThreshold.BLOCK_MEDIUM_AND_ABOVE,
        }
    )

model = setup_gemini()

async def chat(user_message: str, conversation_history: list) -> str:
    """
    Communicates with Gemini API to generate response asynchronously.
    """
    if not model:
        raise Exception("Gemini model is not initialized. Please verify API Key.")
    
    try:
        formatted_history = []
        for msg in conversation_history:
            role = 'user' if msg.get('role') == 'user' else 'model'
            formatted_history.append({'role': role, 'parts': [msg.get('content', '')]})
            
        chat_session = model.start_chat(history=formatted_history)
        response = await chat_session.send_message_async(user_message)
        return response.text
    except Exception as e:
        logger.error(f"Gemini API chat error: {e}")
        raise e
