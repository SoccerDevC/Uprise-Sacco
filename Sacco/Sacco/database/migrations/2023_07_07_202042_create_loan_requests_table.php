<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreateLoanRequestsTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('loan_requests', function (Blueprint $table) {
            $table->integer('loan_application_number')->primary();
            $table->string('member_id');
            $table->decimal('loan_amount', 10, 2);
            $table->integer('payment_period');
            $table->integer('receipt_number')->nullable();

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
        Schema::dropIfExists('loan_requests');
    }
}
