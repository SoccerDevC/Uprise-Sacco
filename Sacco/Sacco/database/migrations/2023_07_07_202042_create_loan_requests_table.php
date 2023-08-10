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
            $table->decimal('recommended_funds', 10, 2);
            $table->string('loan_approval_status');



            
            
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
