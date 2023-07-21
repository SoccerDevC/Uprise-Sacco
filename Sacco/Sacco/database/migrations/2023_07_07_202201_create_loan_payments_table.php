<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreateLoanPaymentsTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('loan_payments', function (Blueprint $table) {
            $table->integer('loan_application_number');
            $table->string('member_id');
            $table->string('loan_id')->primary();
            $table->decimal('repayment_amount', 10, 2);
            $table->date('repayment_date');
           
            $table->integer('receipt_number');

            $table->foreign('loan_application_number')->references('loan_application_number')->on('loan_requests');
            $table->foreign('member_id')->references('member_id')->on('members');
            $table->foreign('receipt_number')->references('receipt_number')->on('contributions');
            
            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('loan_payments');
    }
}
