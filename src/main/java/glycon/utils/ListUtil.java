package glycon.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import glycon.object.FriendlyFirm;

public class ListUtil {

	public static List<FriendlyFirm> sanatizeFriendlyList(List<Future<List<FriendlyFirm>>> resultList) {

		List<FriendlyFirm> friendlyFirmList = new ArrayList<>();

		for (Future<List<FriendlyFirm>> friendlyFirmFutureList : resultList) {

			try {
				friendlyFirmFutureList.get().forEach(friendlyFirm -> {
					if (friendlyFirm != null)
						friendlyFirmList.add(friendlyFirm);

				});
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return friendlyFirmList;

	}

	public static <E> List<List<E>> splitList(int threads, List<E> rawFrimList) {

		List<List<E>> partitions = new ArrayList<>();
		for (int i = 0; i < rawFrimList.size(); i += threads) {
			partitions.add(rawFrimList.subList(i, Math.min(i + threads, rawFrimList.size())));
		}

		return partitions;
	}

	public static List<String> removeDuplicates(List<String> rawFrimList, List<String> friendlyFirmPrevious) {

		rawFrimList.removeAll(new HashSet<>(friendlyFirmPrevious));

		return new ArrayList<>(rawFrimList);
	}

}