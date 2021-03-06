/*
 * Copyright (c) 2014, Cloudera, Inc. All Rights Reserved.
 *
 * Cloudera, Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"). You may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * This software is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for
 * the specific language governing permissions and limitations under the
 * License.
 */

package com.cloudera.oryx.ml.param;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.base.Preconditions;
import org.apache.commons.math3.random.RandomDataGenerator;

final class DiscreteAround implements HyperParamValues<Integer>, Serializable {

  private final int around;
  private final int step;

  DiscreteAround(int around, int step) {
    Preconditions.checkArgument(step > 0);
    this.around = around;
    this.step = step;
  }

  @Override
  public List<Integer> getTrialValues(int num) {
    Preconditions.checkArgument(num > 0);
    if (num == 1) {
      return Collections.singletonList(around);
    }
    List<Integer> values = new ArrayList<>(num);
    int value = around - ((num - 1) * step / 2);
    for (int i = 0; i < num; i++) {
      values.add(value);
      value += step;
    }
    return values;
  }

  /**
   * @param rdg random number generator to use
   * @return a hyperparameter value chosen from Normal(around, step) and rounded to the nearest integer
   */
  @Override
  public Integer getRandomValue(RandomDataGenerator rdg) {
    return (int) Math.round(rdg.nextGaussian(around, step));
  }

  /**
   * @return {@code Long.MIN_VALUE}
   */
  @Override
  public long getNumDistinctValues() {
    return Long.MAX_VALUE;
  }

  @Override
  public String toString() {
    return "DiscreteAround[..." + getTrialValues(3) + "...]";
  }

}
