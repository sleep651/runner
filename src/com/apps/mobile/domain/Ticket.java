package com.apps.mobile.domain;


import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: hejun
 * Date: 11-12-29
 * Time: 下午2:21
 * To change this template use File | Settings | File Templates.
 */
public class Ticket {

    private LinkedHashMap<Long, UserAccount> ticketMap;

    private Ticket() {

        ticketMap = new LinkedHashMap<Long, UserAccount>();
    }

    private static Ticket ref = new Ticket();

    public static synchronized Long getTicketNumber(UserAccount userAccount) {

        if (userAccount == null) {

            throw new IllegalArgumentException("用户账户不能为空");
        }


        if (ref == null) {
            ref = new Ticket();
        }

        //删除原来的登录ticket
        removeUserAccount(userAccount);
        
        Long ticketNumber = Calendar.getInstance().getTime().getTime();

        ref.ticketMap.put(ticketNumber, userAccount);
    	for (Long key : ref.ticketMap.keySet()) {
    		System.out.println("@ticketMap:"+key + " = " + ref.ticketMap.get(key));
    	}
        return ticketNumber;
    }
    public static synchronized Boolean removeUserAccount(UserAccount userAccount) {
        Boolean ret = false;
        if (ref == null) {
            return false;
        }
        LinkedHashMap<Long, UserAccount> map = ref.ticketMap;
        if(map!=null && map.size()>0){
			for(Iterator<Map.Entry<Long, UserAccount>> it = map.entrySet().iterator() ; it.hasNext() ;){
				Map.Entry<Long, UserAccount> entry = it.next() ;
				Long key = entry.getKey() ;
				UserAccount value =  entry.getValue() ;
				//log.info(key + "=" + value) ;
				if(value!=null&&
						value.getUser_id()!=null&&
						value.getUser_id().equals(userAccount.getUser_id())){
        			//刪除原來的ticket
        			System.out.println("remove:"+key);
        			//map.remove(key);
        			it.remove();
        			ret = true;
				}
			}
        }		
    	return ret;
    }
    public static synchronized UserAccount getUserAccount(Long ticketNumber) throws InvalidTicketException {

        if (ticketNumber == null) {

            throw new IllegalArgumentException("ticket 不能为空");

        }

        if (ref.ticketMap.containsKey(ticketNumber)) {

            UserAccount account = ref.ticketMap.get(ticketNumber);

            return account;

        } else {

            throw new InvalidTicketException();
        }
    }

    public static synchronized void destoryTicket(Long ticketNumber) throws InvalidTicketException {

        if (ticketNumber == null) {

            throw new IllegalArgumentException("ticket 不能为空");

        }

        if (ref.ticketMap.containsKey(ticketNumber)) {

            ref.ticketMap.remove(ticketNumber);

        } else {

            throw new InvalidTicketException();

        }
    }

    public static synchronized void checkTicket(Long ticketNumber) throws InvalidTicketException {

        if (ticketNumber == null) {

            throw new IllegalArgumentException("ticket 不能为空");

        }

        if (!ref.ticketMap.containsKey(ticketNumber)) {
            throw new InvalidTicketException();
        }

    }
}
