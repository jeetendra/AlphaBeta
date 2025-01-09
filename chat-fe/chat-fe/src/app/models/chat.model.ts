export interface User {
  id: string;
  email: string;
  username: string;
  // avatar_url?: string;
}

export interface Message {
  id: string;
  sender_id: string;
  chat_id: string;
  content: string;
  media_url?: string;
  created_at: string;
}

export interface Chat {
  id: string;
  name?: string;
  group: boolean;
  createdAt: string;
  participants: User[];
  // last_message?: Message;
}
export interface LoginResponse {
  token: string;
  user: User
}
