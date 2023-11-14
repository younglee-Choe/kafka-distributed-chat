package leele.kafkadistributedchatserver.kafka.process;

import leele.kafkadistributedchatserver.chat.dto.Chat;
import leele.kafkadistributedchatserver.member.entity.Member;
import leele.kafkadistributedchatserver.member.repository.MemberRepository;
import leele.kafkadistributedchatserver.memberRoom.entity.MemberRoom;
import leele.kafkadistributedchatserver.memberRoom.repository.MemberRoomRepository;
import leele.kafkadistributedchatserver.room.entity.Room;
import leele.kafkadistributedchatserver.room.repository.RoomRepository;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ProcessChatMessages implements BeanFactoryAware {
    private static BeanFactory beanFactory;
    private static LocalDateTime joinDate;

    @Override
    public void setBeanFactory(BeanFactory factory) {
        beanFactory = factory;
    }

    public static void findJoinDate(String roomId, String memberId) {
        // Retrieve the repository from the application context in static method
        MemberRepository memberRepository = beanFactory.getBean(MemberRepository.class);
        RoomRepository roomRepository = beanFactory.getBean(RoomRepository.class);
        MemberRoomRepository memberRoomRepository = beanFactory.getBean(MemberRoomRepository.class);

        Member member = memberRepository.findByMemberId(memberId);
        Room room = roomRepository.findByRoomId(roomId);
        MemberRoom memberRoom = memberRoomRepository.findByMemberAndRoom(member, room);

        joinDate = memberRoom.getJoinDate();
    }

    public static List<Chat> processMessages(List<Chat> chatList) {
        long sortingStartTime = System.nanoTime();     // nanoseconds

        mergeSort(chatList, chatList.size());

        long sortingEndTime = System.nanoTime();
        long sortingExecutionTime = sortingEndTime - sortingStartTime;
        System.out.println("Time takend to sort: " + sortingEndTime + " - " + sortingStartTime + " = " + sortingExecutionTime + " nanoseconds");

        long searchingStartTime = System.nanoTime();     // nanoseconds

        List<Chat> processedChatList = processMessageSearch(chatList);

        long searchingEndTime = System.nanoTime();
        long searchingExecutionTime = searchingEndTime - searchingStartTime;
        System.out.println("Time takend to search: " + searchingEndTime + " - " + searchingStartTime + " = " + searchingExecutionTime + " nanoseconds");

        System.out.println("✅[Time taken to process message] " + (sortingExecutionTime + searchingExecutionTime));

        return processedChatList;
    }

    public static List<Chat> processMessageSearch(List<Chat> chatList) {
        if (joinDate.isBefore(chatList.get(0).getDate())) {
            return chatList;
        } else if (joinDate.isAfter(chatList.get(chatList.size() - 1).getDate())) {
            return new ArrayList<>();
        } else {
            return BSearchRecursive(chatList, joinDate, 0, chatList.size() - 1);
        }
    }

    // Binary Search algorithm with recursion
    public static List<Chat> BSearchRecursive(List<Chat> arr, LocalDateTime joinDate, int left, int right) {
        if (left > right) return arr;

        int mid = (left + right) / 2;

        if ((right - left) <= 2) {
            if (joinDate.isBefore(arr.get(mid).getDate())) {
                return arr.subList(mid, arr.size());
            }
            if (joinDate.isAfter(arr.get(mid).getDate())) {
                return arr.subList(mid + 1, arr.size());
            }
        }

        if (arr.get(mid).getDate().isBefore(joinDate))
            return BSearchRecursive(arr, joinDate, mid + 1, right);
        else if (arr.get(mid).getDate().isAfter(joinDate))
            return BSearchRecursive(arr, joinDate, left, mid - 1);
        else
            return arr.subList(mid, arr.size());
    }

    // Merge sort algorithm
    public static void mergeSort(List<Chat> arr, int n) {
        if (n < 2) {
            return;
        }
        int mid = n / 2;
        List<Chat> l = new ArrayList<>(mid);
        List<Chat> r = new ArrayList<>(n - mid);

        for (int i = 0; i < mid; i++) {
            l.add(i, arr.get(i));
        }
        for (int i = mid; i < n; i++) {
            r.add(i - mid, arr.get(i));
        }

        mergeSort(l, mid);
        mergeSort(r, n - mid);

        merge(arr, l, r, mid, n - mid);
    }

    public static void merge(List<Chat> arr, List<Chat> l, List<Chat> r, int left, int right) {
        int i = 0, j = 0, k = 0;
        while (i < left && j < right) {
            if (l.get(i).getDate().compareTo(r.get(j).getDate()) <= 0) {
                arr.set(k++, l.get(i++));
            }
            else {
                arr.set(k++, r.get(j++));
            }
        }
        while (i < left) {
            arr.set(k++, l.get(i++));
        }
        while (j < right) {
            arr.set(k++, r.get(j++));
        }
    }
}
