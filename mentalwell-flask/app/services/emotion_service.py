import os
import json
import logging
import google.generativeai as genai

logger = logging.getLogger(__name__)

PROMPT = """
Analyze the emotional tone of the following text and state the primary emotion. 
Return ONLY a valid JSON object in this exact format, with no markdown code blocks:
{"emotion": "happy/sad/anxious/angry/stressed/neutral/hopeful", "confidence": <float between 0-1>, "suggestions": ["suggestion1", "suggestion2"]}

Text to analyze:
"{text}"
"""

model = None
api_key = os.environ.get('GEMINI_API_KEY')
if api_key:
    genai.configure(api_key=api_key)
    model = genai.GenerativeModel('gemini-1.5-flash')

async def analyze_emotion(text: str) -> dict:
    if not model:
        raise Exception("Gemini model is not initialized.")
    try:
        prompt = PROMPT.format(text=text)
        response = await model.generate_content_async(prompt)
        text_response = response.text.strip()
        
        if text_response.startswith('```json'):
            text_response = text_response[7:-3]
        elif text_response.startswith('```'):
            text_response = text_response[3:-3]
            
        result = json.loads(text_response.strip())
        return result
    except json.JSONDecodeError as e:
        logger.error(f"Failed to parse Gemini emotion response: {response.text}")
        return {"emotion": "neutral", "confidence": 0.5, "suggestions": ["Take a deep breath and reflect on your feelings."]}
    except Exception as e:
        logger.error(f"Gemini API emotion error: {e}")
        raise e
