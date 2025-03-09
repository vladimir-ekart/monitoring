package com.vladimirekart.monitoring.api.useCases;

import com.vladimirekart.monitoring.api.entity.User;

public interface UseCase<TInput, TResult> {
  TResult run(TInput input, User user) throws Exception;
}
