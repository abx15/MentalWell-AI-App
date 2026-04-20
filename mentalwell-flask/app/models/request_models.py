from dataclasses import dataclass
from typing import List

@dataclass
class ChatRequest:
    message: str
    user_id: str
    conversation_history: List[dict]

@dataclass
class AnalyzeRequest:
    text: str
    user_id: str

@dataclass
class SuggestRequest:
    mood: str
    user_id: str
    recent_emotions: List[str]
