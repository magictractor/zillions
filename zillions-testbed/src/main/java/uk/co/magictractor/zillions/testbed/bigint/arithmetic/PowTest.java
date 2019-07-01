/**
 * Copyright 2015-2019 Ken Dobson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.co.magictractor.zillions.testbed.bigint.arithmetic;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import uk.co.magictractor.jura.WithinSuite;
import uk.co.magictractor.zillions.api.BigInt;
import uk.co.magictractor.zillions.environment.BigIntFactory;
import uk.co.magictractor.zillions.testbed.bigint.OpTestIntParam;

@WithinSuite
public class PowTest extends OpTestIntParam {

    //    private static final String EXPECTED_LARGE = "6999473305594979460747523383072889601477236216184911003419461290466710139114217"
    //            + "7498802869032252596121617003960344726419808980431015122422932744543093286338699"
    //            + "2920781367008222650828413018983664862354307564686330615731549345907362894295691"
    //            + "9283780934536469658033947258282355331051697332100436294431152385975663408620148"
    //            + "9620203009827040696187124524614354968985290155557599502294365672784884844668998"
    //            + "4724830523204379355169395364193814653054353355309812321262882637276207862538733"
    //            + "0268407440534048215528764519083213609790473682884868474150785139292321067433281"
    //            + "8692147713973166563496268541776379133065981498812704856507864801567390714661119"
    //            + "557507861640110888652146301889";

    private static final String EXPECTED_LARGE = "7965501558961948470995544680454056876116293243206450225928081162654424575673391"
            + "2699892211976635826648408458849290694354467596506857819339171418486385907127459"
            + "4166823911249595005793230856231415221469216930907334010672175587253152474682507"
            + "1814406770871335157602254073222871907885736678961030541375699436580171219003901"
            + "4651036646101438160588422424418883171362517769549045298585908484752820593116795"
            + "1955323715165905603214399303058363174323976337859980657672898456708950881312602"
            + "2962906692550996096828767840916051000911136090851829563252342132164177679972814"
            + "7806009097800713005747015547538652660442637626701680339298663586953139804559610"
            + "636090647296094054047";

    public PowTest() {
        super(BigInt::pow);
    }

    @Test
    public void testPowZeroZero() {
        check(0, 0, 1);
    }

    @Test
    public void testPowOneZero() {
        check(1, 0, 1);
    }

    @Test
    public void testLargePositive() {
        check("878723487", "73", EXPECTED_LARGE);
    }

    @Test
    public void testLargeNegative() {
        check("-878723487", "73", "-" + EXPECTED_LARGE);
    }

    @Test
    public void testNegativeExponent() {
        Assertions.assertThatThrownBy(() -> BigIntFactory.from(1).pow(-1))
                .isExactlyInstanceOf(ArithmeticException.class)
                .hasMessage("Negative exponent");
    }

}
