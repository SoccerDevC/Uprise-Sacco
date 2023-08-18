<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;
use Illuminate\Support\Facades\DB; // Import the DB facade


class CreateRegisteredLoansTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('registered_loans', function (Blueprint $table) {
            $table->integer('loan_application_number')->primary();
            $table->integer('payment_period')->nullable();
            $table->date('installment_date')->nullable();
            $table->string('member_id', 255)->nullable();
            $table->timestamps(); // If you want to include timestamps for when records are created/updated
        });

        // Inserting data into the table
        DB::table('registered_loans')->insert([
            [
                'loan_application_number' => 1319437686,
                'payment_period' => 4,
                'installment_date' => '2023-08-06',
                'member_id' => 'M010',
            ],
            [
                'loan_application_number' => 1319562627,
                'payment_period' => 3,
                'installment_date' => '2023-08-06',
                'member_id' => 'M011',
            ],
            [
                'loan_application_number' => 1319606820,
                'payment_period' => 4,
                'installment_date' => '2023-09-06',
                'member_id' => 'M012',
            ],
            [
                'loan_application_number' => 1588986730,
                'payment_period' => 3,
                'installment_date' => '2023-08-09',
                'member_id' => 'M014',
            ],
        ]);
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('registered_loans');
    }
}
