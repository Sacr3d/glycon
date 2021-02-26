package glycon.utils;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarStyle;

public class ProgressUtil {

	public static <E> void displayProgressBar(AtomicInteger atomicInt, List<E> frimList,
			ExecutorService executorService, String message) {

		try (ProgressBar pb = new ProgressBar(message, frimList.size(),
				ProgressBarStyle.ASCII)) {

			while (!executorService.isTerminated()) {

				pb.stepTo(atomicInt.get());

			}

		}
	}

}
