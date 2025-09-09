class Logger {

     private static class MessageEntry{
        String message;
        int timestamp;

        MessageEntry(String message,int timestamp){
            this.message = message;
            this.timestamp = timestamp;
        }
    }

    private static int WINDOW=10;
    private Map<String,Integer> storedTimestamp;
    private Queue<MessageEntry> lastKTime;
   
    public Logger() {
        this.storedTimestamp = new HashMap<>();
        this.lastKTime = new LinkedList<>();
    }
    
    public boolean shouldPrintMessage(int timestamp, String message) {
        while(!lastKTime.isEmpty() && lastKTime.peek().timestamp<=(timestamp-WINDOW)){
            MessageEntry oldMessage = lastKTime.poll();
            storedTimestamp.remove(oldMessage.message);
        }
        if(!storedTimestamp.containsKey(message)){
            storedTimestamp.put(message,timestamp);
            lastKTime.offer(new MessageEntry(message,timestamp));
            return true;
        }
        return false;
    }
}

/**
 * Your Logger object will be instantiated and called as such:
 * Logger obj = new Logger();
 * boolean param_1 = obj.shouldPrintMessage(timestamp,message);
 */
