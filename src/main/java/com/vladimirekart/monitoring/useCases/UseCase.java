package com.vladimirekart.monitoring.useCases;

import com.vladimirekart.monitoring.entity.User;

public interface UseCase<TInput, TResult> {
  TResult run(TInput input, User user) throws Exception;
}
