package glycon.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import glycon.object.Firm;
import glycon.object.FriendlyFirm;

public class ListUtil {

	public static <E> List<Firm> createWorkingFirmList(List<E> rawFrimList, List<Firm> primeFirmList) {

		return primeFirmList.stream().filter(firm -> rawFrimList.contains(firm.getFirmId()))
				.collect(Collectors.toList());

	}

	public static <E> List<E> removeDuplicates(List<E> rawFrimList, List<E> friendlyFirmPrevious) {

		List<E> listCopy = new ArrayList<>(rawFrimList);

		listCopy.removeAll(new HashSet<>(friendlyFirmPrevious));

		return new ArrayList<>(listCopy);
	}

	public static List<FriendlyFirm> sanatizeFriendlyList(List<FriendlyFirm> finalFriendlyFirmList) {

		List<FriendlyFirm> friendlyFirmList = new ArrayList<>();

		finalFriendlyFirmList.forEach(friendlyFirm -> {
			if (friendlyFirm != null)
				friendlyFirmList.add(friendlyFirm);

		});

		return friendlyFirmList;

	}

	public static <E> List<List<E>> splitList(int threads, List<E> rawFrimList) {

		List<List<E>> partitions = new ArrayList<>();
		for (int i = 0; i < rawFrimList.size(); i += threads) {
			partitions.add(rawFrimList.subList(i, Math.min(i + threads, rawFrimList.size())));
		}

		return partitions;
	}

	private ListUtil() {
		throw new IllegalStateException("Utility class");
	}

}