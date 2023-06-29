package info.jeonju.stylerent.auth.ChattingModels;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import info.jeonju.stylerent.R;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter  extends BaseAdapter  {
    private List<SenderHistory> senderList;
    private List<ReceiverHistory> receiverList;
    private List<Message> messageList;
    private LayoutInflater inflater;

    private Context context;

    public MessageAdapter(Context context, List<SenderHistory> senderList, List<ReceiverHistory> receiverList) {
        this.senderList = senderList;
        this.receiverList = receiverList;
        this.context = context;
        inflater = LayoutInflater.from(context);
        messageList = createMessageList(senderList, receiverList);


//        // Combine the senderList and receiverList into a single list and sort by chatId
//        messageList = new ArrayList<>();
//        messageList.addAll(senderList);
//        messageList.addAll(receiverList);
//        Collections.sort(messageList, new Comparator<Message>() {
//            @Override
//            public int compare(Message message1, Message message2) {
//                return message1.getChattingId().compareTo(message2.getChattingId());
//            }
//        });
    }

    private List<Message> createMessageList(List<SenderHistory> senderList, List<ReceiverHistory> receiverList) {
        List<Message> messageList = new ArrayList<>();

        // Merge senderList and receiverList into a single messageList based on chatId
        int senderIndex = 0;
        int receiverIndex = 0;
        while (senderIndex < senderList.size() && receiverIndex < receiverList.size()) {
            SenderHistory senderMessage = senderList.get(senderIndex);
            ReceiverHistory receiverMessage = receiverList.get(receiverIndex);

            if (senderMessage.getChatId() <= receiverMessage.getChatId()) {
                Message message = new Message(senderMessage.getChatId(), senderMessage.getUserId(), senderMessage.getMessage(), "SENDER");
                messageList.add(message);
                senderIndex++;
            } else {
                Message message = new Message(receiverMessage.getChatId(), receiverMessage.getUserId(), receiverMessage.getMessage(), "RECEIVER");
                messageList.add(message);
                receiverIndex++;
            }
        }

        // Add remaining sender messages, if any
        while (senderIndex < senderList.size()) {
            SenderHistory senderMessage = senderList.get(senderIndex);
            Message message = new Message(senderMessage.getChatId(), senderMessage.getUserId(), senderMessage.getMessage(), "SENDER");
            messageList.add(message);
            senderIndex++;
        }

        // Add remaining receiver messages, if any
        while (receiverIndex < receiverList.size()) {
            ReceiverHistory receiverMessage = receiverList.get(receiverIndex);
            Message message = new Message(receiverMessage.getChatId(), receiverMessage.getUserId(), receiverMessage.getMessage(), "RECEIVER");
            messageList.add(message);
            receiverIndex++;
        }

        return messageList;
    }


    public void setSenderList(List<SenderHistory> senderList) {
        this.senderList = senderList;
        messageList = createMessageList(senderList, receiverList);
    }

    public void setReceiverList(List<ReceiverHistory> receiverList) {
        this.receiverList = receiverList;
        messageList = createMessageList(senderList, receiverList);
    }



    public List<Message> getMessageList() {
        return messageList;
    }

    @Override
    public int getCount() {
        return messageList.size();
    }

    @Override
    public Message getItem(int position) {
        return messageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_message, parent, false);
            holder = new ViewHolder();
            holder.receiverMessageTextView = convertView.findViewById(R.id.receiverMessage);
            holder.senderMessageTextView = convertView.findViewById(R.id.senderMessage);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Message message = messageList.get(position);

        if (message.getMessageType().equals("SENDER")) {
            holder.receiverMessageTextView.setVisibility(View.GONE);
            holder.senderMessageTextView.setVisibility(View.VISIBLE);
            holder.senderMessageTextView.setText(message.getMessage());
        } else if (message.getMessageType().equals("RECEIVER")){
            holder.receiverMessageTextView.setVisibility(View.VISIBLE);
            holder.senderMessageTextView.setVisibility(View.GONE);
            holder.receiverMessageTextView.setText(message.getMessage());
        }
        return convertView;
    }


    private static class ViewHolder {
        TextView receiverMessageTextView;
        TextView senderMessageTextView;

    }
}