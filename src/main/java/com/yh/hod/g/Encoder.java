package com.yh.hod.g;

import kafka.message.Message;

interface Encoder<T> {  
  public Message toMessage(T data);  
}  